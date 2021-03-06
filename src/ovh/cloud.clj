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

(defn list-regions
    [project]
    (ovh/validate
        (ovh/call {:method "GET"
                   :resource (str api-path "/project/" project "/region")}) 200))

(defn region-state
    [project region]
    (ovh/validate
        (ovh/call {:method "GET"
                   :resource (str api-path "/project/" project "/region/" region)}) 200))

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

 (defn describe-instance
    "Describe instance"
    [project instance]
    (let [resource {:method "GET"
                    :resource (str api-path "/project/" project "/instance/" instance)}]
    (ovh/validate
        (ovh/call resource)
        200)))

(defn rename-instance
    [project instance name]
    (let [resource {:method "PUT"
                    :resource (str api-path "/project/" project "/instance/" instance)
                    :body {:instanceName name}}]
        (ovh/validate
            (ovh/call resource)
            202)))

(defn reboot-instance
  [project instance-id & [type]]
  (ovh/call {:method "POST"
             :resource (str api-path "/project/" project "/instance/" instance-id "/reboot")
             :body (if (#{"soft" "hard"} type) {:type type} {})}))

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
