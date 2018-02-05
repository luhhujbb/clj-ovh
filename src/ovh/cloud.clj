(ns ovh.cloud
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [clj-http.util :as util]
            [ovh.core :as ovh]))

(def api-path "/cloud")

(defn describe-project
  [project]
  (ovh/validate
    (ovh/call {:method "GET"
               :ressource (str api-path "/project/" project)}) 200))

(defn describe-instances
  "Return details of asked ip"
  [project & [region]]
  (ovh/validate
      (ovh/call {:method "GET"
                 :ressource (str api-path "/project/" project "/instance")})
      200))

(defn describe-private-networks
  "Return details of asked ip"
  [project]
  (ovh/validate
      (ovh/call {:method "GET"
                 :ressource (str api-path "/project/" project "/network/private")})
      200))

(defn describe-private-network-subnets
  "Return details of asked ip"
  [project private-network]
  (ovh/validate
      (ovh/call {:method "GET"
                 :ressource (str api-path "/project/" project "/network/private/" private-network "/subnet")})
      200))
