(ns ovh.cloud
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [clj-http.util :as util]
            [ovh.core :as ovh]))

(def api-path "/cloud")

(defn list-projects
  []
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/project")}) 200))

(defn describe-project
  [project]
  (ovh/validate
    (ovh/call {:method "GET"
               :resource (str api-path "/project/" project)}) 200))

(defn describe-instances
  "Describe list of instances"
  [project & [region]]
  (let [resource {:method "GET"
                  :resource (str api-path "/project/" project "/instance")}
        resource (if region (assoc resource :query-params {:region region})
                    resource)]
  (ovh/validate
      (ovh/call resource)
      200)))

(defn describe-private-networks
  "Describe list of private network"
  [project]
  (ovh/validate
      (ovh/call {:method "GET"
                 :resource (str api-path "/project/" project "/network/private")})
      200))

(defn describe-private-network-subnets
  "DEscribe private subnet of a private network"
  [project private-network]
  (ovh/validate
      (ovh/call {:method "GET"
                 :resource (str api-path "/project/" project "/network/private/" private-network "/subnet")})
      200))
