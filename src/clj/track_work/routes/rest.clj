(ns track-work.routes.rest
  (:require [track-work.layout :as layout]
            [schema.core :as s]
            [ring.swagger.schema :as rs]
            [track-work.db.core :as db]
            [compojure.core :refer [defroutes GET]]
            [compojure.api.sweet :as sweet]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))
(def pong
  (sweet/api
   (sweet/GET "/pong" []
              (response/ok {:pong "ping"}))))
(def get-users
  (sweet/api
   (sweet/GET "/get_users" []
              (response/ok (do
                             (println "hello")
                             (db/get-users))))))

(def get-timetypes
  (sweet/api
   (sweet/GET "/get_types" []
              (response/ok (db/get-timetypes)))))

(def add-time-type
  (sweet/api
   (sweet/POST "/add_type" [] 
               :query-params [time_type :- String
                              description :- String]
               (response/ok
                (do
                  (println (str "echo! " "\"" time_type "\"" description))
                  (db/create-timetype! {:time_type time_type :description description})
                  time_type)))))

(defroutes rest-routes
  get-users
  get-timetypes
  add-time-type
  pong)
