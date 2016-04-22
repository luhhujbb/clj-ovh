(ns ovh.vrack
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [ovh.core :as ovh]))

(def api-path "/vrack")

(defn list-vracks
  []
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource api-path})
    {:status 403}))

(defn describe
  [vrack-name]
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" vrack-name)})
    {:status 403}))

(defn list-servers
  [vrack-name]
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" vrack-name "/dedicatedServer")})
    {:status 403}))

(defn add-server
  [vrack-name server-name]
  (if (ovh/initialized?)
    (ovh/call {:method "POST"
               :ressource (str api-path "/" vrack-name "/dedicatedServer")
               :body {:serviceName vrack-name
                      :dedicatedServer server-name}})
    {:status 403}))

(defn delete-server
  [vrack-name server-name]
    (if (ovh/initialized?)
    (ovh/call {:method "DELETE"
               :ressource (str api-path "/" vrack-name "/dedicatedServer/" server-name)
               :body {}})
    {:status 403}))
