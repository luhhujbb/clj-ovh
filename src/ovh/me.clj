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
    (ovh/call {:method "GET"
               :ressource (str api-path "/sshKey")})
    200 []))

(defn add-ssh-key
  [key-name pub-key]
    (ovh/call {:method "POST"
               :ressource (str api-path "/sshKey")
               :body {:key pub-key
                      :keyName key-name}}))

(defn get-ssh-key
  [key-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :ressource (str api-path "/sshKey/" key-name)})
    200))

(defn del-ssh-key
  [key-name]
    (ovh/call {:method "DELETE"
               :ressource (str api-path "/sshKey/" key-name)}))

;;########################
;;Partition schemes
;;########################
(defn list-installation-templates
  []
  (ovh/validate
    (ovh/call {:method "GET"
               :ressource (str api-path "/installationTemplate")})
    200 []))

(defn get-template-partition-schemes
  [template-name]
  (ovh/validate
    (ovh/call {:method "GET"
               :ressource (str api-path "/installationTemplate/" template-name "/partitionScheme")})
    200 []))
