(ns clj-ovh.core
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [clj-http.client :as http]))


(def creds (atom {}))

(def endpoint "https://api.ovh.com/1.0")

(defn timestamp
  []
  (str (long (/ (System/currentTimeMillis) 1000))))

(def mk-signature
  [method query body ts]
  (let [prefix "$1$"
        digest-string (str (:app-secret @creds) "+" (:consumer-key @creds) "+" method "+" body "+" ts)]
    (str prefix (digest/sha-1 digest-string))))

(def mk-headers
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
        headers (mk-headers "GET" url "" ts)]
        (http/get url {:headers headers})))

(defmethod call "PUT" [params]
  (let [url (str endpoint (:ressource params))
      ts (timestamp)
      body (generate-string (:body params))
      headers (mk-headers "PUT" url body ts)]
      (http/put url {:body body :headers headers})))

(defmethod call "POST" [params]
  (let [url (str endpoint (:ressource params))
      ts (timestamp)
      body (generate-string (:body params))
      headers (mk-headers "POST" url body ts)]
      (http/post url {:body body :headers headers})))

(defmethod call "DELETE" [params]
 (let [url (str endpoint (:ressource params))
    ts (timestamp)
    body (generate-string (:body params))
    headers (mk-headers "DELETE" url body ts)]
    (http/delete url {:body body :headers headers})))

(defmethod call :default [params]
  (log/info "Unsupported method"))

(defn init!
  [app-key app-secret consumer-key]
  (swap! creds assoc :app-key app-key
                     :app-secret app-secret
                     :consumer-key consumer-key))
