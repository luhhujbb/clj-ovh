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

(defn add-server
  [vrack-name server-name]
    (ovh/call {:method "POST"
               :ressource (str api-path "/" vrack-name "/dedicatedServer")
               :body {:dedicatedServer server-name}}))

(defn delete-server
  [vrack-name server-name]
    (ovh/call {:method "DELETE"
               :ressource (str api-path "/" vrack-name "/dedicatedServer/" server-name)
               :body {}}))
