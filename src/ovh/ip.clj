(ns ovh.ip
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [clj-http.util :as util]
            [ovh.core :as ovh]))

(def api-path "/ip")

(defn get-ips
  "Return Ip of current account"
  []
  (ovh/validate
    (if (ovh/initialized?)
      (ovh/call {:method "GET"
                 :ressource (str api-path)})
      {:status 403}) 200))

(defn get-ip-details
  "Return details of asked ip"
  [ip]
  (ovh/validate
    (if (ovh/initialized?)
      (ovh/call {:method "GET"
                 :ressource (str api-path "/" (util/url-encode ip))})
      {:status 403}) 200))

(defn set-ip-reverse
  "Set the reverse for one ip in the pool"
  [ip-pool ip reverse]
  (if (ovh/initialized?)
    (ovh/call {:method "POST"
               :ressource (str api-path "/" (util/url-encode ip) "/reverse")
               :body {:ipReverse ip
                      :reverse reverse}})
    {:status 403}))
