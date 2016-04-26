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
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/sshKey")})
    {:status 403}))

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
  (if (ovh/initialized?)
    (ovh/call {:method "GET"
               :ressource (str api-path "/sshKey/" key-name)})
    {:status 403}))

(defn del-ssh-key
  [key-name]
    (if (ovh/initialized?)
    (ovh/call {:method "DELETE"
               :ressource (str api-path "/sshKey/" key-name)})
    {:status 403}))
