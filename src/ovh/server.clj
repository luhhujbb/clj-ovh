(ns ovh.server
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [ovh.core :as ovh]))

(def api-path "/dedicated/server")

(defn list-servers
  []
  (ovh/validate
    (if (ovh/initialized?)
      (ovh/call {:method "GET"
               :ressource api-path})
      {:status 403}) 200 nil))

(defn describe
  [server-name]
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" server-name)})
    {:status 403}) 200))

(defn vrack
  [server-name]
  (ovh/validate
    (if (ovh/initialized?)
      (ovh/call {:method "GET"
               :ressource (str api-path "/" server-name "/vrack")})
      {:status 403})  200 []))

(defn list-templates
  [server-name]
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" server-name "/install/compatibleTemplates")})
    {:status 403}) 200))

(defn list-partition-schemes
  [server-name]
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" server-name "/install/compatibleTemplatePartitionSchemes")})
    {:status 403}) 200 []))

(defn hardware-raid-profile
  [server-name]
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" server-name "/install/hardwareRaidProfile")})
    {:status 403}) 200))

(defn ip-specifications
  [server-name]
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" server-name "/specifications/ip")})
    {:status 403}) 200))

(defn hardware-specifications
  [server-name]
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" server-name "/specifications/hardware")})
    {:status 403}) 200))

(defn network-specifications
  [server-name]
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" server-name "/specifications/network")})
    {:status 403}) 200))

(defn install
  [server-name partitionSchemeName templateName details]
  (if (ovh/initialized?)
    (ovh/call {:method "POST"
               :ressource (str api-path "/" server-name "/install/start")
               :body {:partitionSchemeName partitionSchemeName
                      :templateName templateName
                      :details details}})
    {:status 403}))

(defn install-status
  [server-name]
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/" server-name "/install/status")})
    {:status 403}) 200))

(defn reboot
  [server-name]
  (if (ovh/initialized?)
    (ovh/call {:method "POST"
               :ressource (str api-path "/" server-name "/reboot")
               :body {}})
    {:status 403}))
