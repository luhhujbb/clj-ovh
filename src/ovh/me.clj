(ns ovh.me
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [cheshire.core :refer :all]
            [ovh.core :as ovh]))

(def api-path "/me")

;;#####################
;;ssh key
;;#####################
(defn list-ssh-keys
  []
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/sshKey")})
    {:status 403}) 200 []))

(defn add-ssh-key
  [key-name pub-key]
    (if (ovh/initialized?)
    (ovh/call {:method "POST"
               :ressource (str api-path "/sshKey")
               :body {:key pub-key
                      :keyName key-name}})
    {:status 403}))

(defn get-ssh-key
  [key-name]
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/sshKey/" key-name)})
    {:status 403}) 200))

(defn del-ssh-key
  [key-name]
    (if (ovh/initialized?)
    (ovh/call {:method "DELETE"
               :ressource (str api-path "/sshKey/" key-name)})
    {:status 403}))

;;########################
;;Partition schemes
;;########################
(defn list-installation-templates
  []
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/installationTemplate")})
    {:status 403}) 200 []))

(defn get-template-partition-schemes
  [template-name]
  (ovh/validate
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/installationTemplate/" template-name "/partitionScheme")})
    {:status 403}) 200 []))
