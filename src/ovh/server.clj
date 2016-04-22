(ns ovh.server
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [ovh.core :as ovh]))

(def api-path "/dedicated/server")

(defn list-servers
  []
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource api-path})
    {:status 403}))

(defn describe
  [server-name]
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" server-name)})
    {:status 403}))
