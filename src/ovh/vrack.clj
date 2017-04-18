(ns ovh.vrack
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [ovh.core :as ovh]))

(def api-path "/vrack")

(defn list-vracks
  []
  (ovh/validate
    (ovh/call {:method "GET"
               :ressource api-path})
    200))

(defn describe
  [vrack-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :ressource (str api-path "/" vrack-name)})
    200))

(defn list-servers
  [vrack-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :ressource (str api-path "/" vrack-name "/dedicatedServer")})
    200))

(defn list-servers-interface
  [vrack-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :ressource (str api-path "/" vrack-name "/dedicatedServerInterfaceDetails")})
    200))

(defn get-server-interface
  [dedicated-server-interface-list server-name]
  (:dedicatedServerInterface
      (first
        (filter
          #(= server-name (:dedicatedServer %))
          dedicated-server-interface-list))))

(defn list-cloud-project
  [vrack-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :ressource (str api-path "/" vrack-name "/cloudProject")})
    200))

(defn list-allowed-services
  [vrack-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :ressource (str api-path "/" vrack-name "/allowedServices")})
    200))

(defn add-server
  "Add a server into vrack (both new and old way with server interface)"
  [vrack-name server-name]
    (let [allowed-services (list-allowed-services vrack-name)]
      (if (.contains (:dedicatedServer allowed-services) server-name)
        (ovh/call {:method "POST"
                   :ressource (str api-path "/" vrack-name "/dedicatedServer")
                   :body {:dedicatedServer server-name}})
        (when-let [dedicated-server-interface (get-server-interface
                                                (:dedicatedServerInterface allowed-services)
                                                server-name)]
          (ovh/call {:method "POST"
                   :ressource (str api-path "/" vrack-name "/dedicatedServerInterface")
                   :body {:dedicatedServerInterface dedicated-server-interface}})))))


(defn delete-server
  [vrack-name server-name]
  "Remove a server from vrack (both new and old way with server interface)"
    (let [server-list (list-servers vrack-name)]
      (if (.contains server-list server-name)
        (ovh/call {:method "DELETE"
                   :ressource (str api-path "/" vrack-name "/dedicatedServer/" server-name)
                   :body {}})
        (let [server-interface-list (list-servers-interface vrack-name)]
          (when-let [dedicated-server-interface (get-server-interface
                                                  server-interface-list
                                                  server-name)]
            (ovh/call :method "DELETE"
                       :ressource (str api-path "/" vrack-name "/dedicatedServerInterface/" dedicated-server-interface)
                       :body {}))))))
