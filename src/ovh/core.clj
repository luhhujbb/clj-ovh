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
    (do
      (log/info res)
      fallback-value))))

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

(defmulti call (fn [params] (:method params)))

(defmethod call "GET" [params]
  (let [url (str endpoint (:ressource params))
        ts (timestamp)
        headers (mk-headers "GET" url "" ts)
        opts (merge {:headers headers} request-conf)]
        (http/get url opts)))

(defmethod call "PUT" [params]
  (let [url (str endpoint (:ressource params))
      ts (timestamp)
      body (generate-string (:body params))
      headers (mk-headers "PUT" url body ts)
      opts (merge {:body body :headers headers} request-conf)]
      (http/put url opts)))

(defmethod call "POST" [params]
  (let [url (str endpoint (:ressource params))
      ts (timestamp)
      body (generate-string (:body params))
      headers (mk-headers "POST" url body ts)
      opts (merge {:body body :headers headers} request-conf)]
      (http/post url opts)))

(defmethod call "DELETE" [params]
 (let [url (str endpoint (:ressource params))
    ts (timestamp)
    body (generate-string (:body params))
    headers (mk-headers "DELETE" url body ts)
    opts (merge {:body body :headers headers} request-conf)]
    (http/delete url opts)))

(defmethod call :default [params]
  (log/info "Unsupported http verb"))

(defn init!
  [app-key app-secret consumer-key]
  (swap! creds assoc :app-key app-key
                     :app-secret app-secret
                     :consumer-key consumer-key)
  (reset! initialized true))
