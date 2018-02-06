(ns ovh.server
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [ovh.core :as ovh]))

(def api-path "/dedicated/server")

(defn list-servers
  []
  (ovh/validate
      (ovh/call {:method "GET"
                 :resource api-path})
      200 nil))

(defn describe
  [server-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/" server-name)})
    200))

(defn vrack
  [server-name]
  (ovh/validate
      (ovh/call {:method "GET"
               :resource (str api-path "/" server-name "/vrack")})
      200 []))

(defn list-templates
  [server-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/" server-name "/install/compatibleTemplates")})
    200))

(defn list-partition-schemes
  [server-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/" server-name "/install/compatibleTemplatePartitionSchemes")})
    200 []))

(defn hardware-raid-profile
  [server-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/" server-name "/install/hardwareRaidProfile")})
    200))

(defn ip-specifications
  [server-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/" server-name "/specifications/ip")})
    200))

(defn hardware-specifications
  [server-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/" server-name "/specifications/hardware")})
    200))

(defn network-specifications
  [server-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/" server-name "/specifications/network")})
    200))

(defn service-infos
  [server-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/" server-name "/serviceInfos")})
    200))

(defn install
  [server-name partitionSchemeName templateName details]
    (ovh/call {:method "POST"
               :resource (str api-path "/" server-name "/install/start")
               :body {:partitionSchemeName partitionSchemeName
                      :templateName templateName
                      :details details}}))

(defn install-status
  [server-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/" server-name "/install/status")})
    200))

(defn reboot
  [server-name]
    (ovh/call {:method "POST"
               :resource (str api-path "/" server-name "/reboot")
               :body {}}))
