(ns ovh.core
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [clj-http.client :as http]
            [digest]))


(def creds (atom {}))
(def initialized (atom false))

(defn initialized?
  []
  @initialized)

(def endpoint "https://api.ovh.com/1.0")

(def request-conf {:accept :json
                   :as :json
                   :throw-exceptions false})

(defn validate
  ([res code]
    (validate res code nil))
  ([res code fallback-value]
  (if (= code (:status res))
    (:body res)
    fallback-value)))

(defn timestamp
  []
  (str (long (/ (System/currentTimeMillis) 1000))))

(defn mk-digest-string
  [method query body ts]
  (str (:app-secret @creds) "+" (:consumer-key @creds) "+" method "+" query "+" body "+" ts))

(defn mk-signature
  [method query body ts]
  (let [prefix "$1$"
        digest-string (mk-digest-string method query body ts)]
    (str prefix (digest/sha-1 digest-string))))

(defn mk-headers
  [method query body ts]
  {"Content-Type" "application/json"
   "X-Ovh-Application" (:app-key @creds)
   "X-Ovh-Timestamp" ts
   "X-Ovh-Signature" (mk-signature method query body ts)
   "X-Ovh-Consumer" (:consumer-key @creds)})

(defn call-with-body
  "Generic method for http verb with body"
  [params]
  (if (initialized?)
    (let [url (str endpoint (:ressource params))
      ts (timestamp)
      body (generate-string (:body params))
      headers (mk-headers (:method params) url body ts)
      opts (merge {:body body :headers headers} request-conf)]
      (try
        (condp = (:method params)
          "POST" (http/post url opts)
          "PUT"  (http/put url opts)
          "DELETE" (http/delete url opts))
       (catch Exception e
         (log/error "Ressource : "url "- Error :" e)
         {:status 500})))
   {:status 403}))

(defmulti call (fn [params] (:method params)))

(defmethod call "GET" [params]
  (if (initialized?)
    (let [url (str endpoint (:ressource params))
        ts (timestamp)
        headers (mk-headers "GET" url "" ts)
        opts (merge {:headers headers} request-conf)]
        (try
          (http/get url opts)
          (catch Exception e
            (log/error "Ressource : "url "- Error :" e)
            {:status 500})))
    {:status 403}))

(defmethod call "PUT" [params]
  (call-with-body params))

(defmethod call "POST" [params]
  (call-with-body params))

(defmethod call "DELETE" [params]
  (call-with-body params))

(defmethod call :default [params]
  (log/info "Unsupported http verb"))

(defn init!
  [app-key app-secret consumer-key]
  (swap! creds assoc :app-key app-key
                     :app-secret app-secret
                     :consumer-key consumer-key)
  (reset! initialized true))
