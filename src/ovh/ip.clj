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
    (ovh/call {:method "GET"
               :resource (str api-path)}) 200))

(defn get-ip-details
  "Return details of asked ip"
  [ip]
  (ovh/validate
      (ovh/call {:method "GET"
                 :resource (str api-path "/" (util/url-encode ip))})
      200))

(defn set-ip-reverse
  "Set the reverse for one ip in the pool"
  [ip-pool ip reverse]
  (ovh/call {:method "POST"
               :resource (str api-path "/" (util/url-encode ip-pool) "/reverse")
               :body {:ipReverse ip
                      :reverse reverse}}))
