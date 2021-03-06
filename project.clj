(defproject luhhujbb/clj-ovh "0.1.13"
  :description "clojure OVH Client"
  :url "https://github.com/luhhujbb/clj-ovh"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                                  ;; logging stuff
                                  [org.clojure/tools.logging "0.3.1"]
                                  [org.slf4j/slf4j-api "1.7.19"]
                                  [org.slf4j/slf4j-log4j12 "1.7.19"]
                                  [org.slf4j/jcl-over-slf4j "1.7.19"]
                                  [log4j "1.2.17"]
                                  [clj-time "0.11.0"]
                                  ;;for dd
                                  [clj-http "3.7.0"]
                                  [cheshire "5.8.0"]
                                  [digest "1.4.4"]]
  :aot :all
  :pedantic? :warn)
