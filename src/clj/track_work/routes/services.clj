(ns track-work.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [track-work.db.core :as db]
            [clojure.xml :as xml]))

(s/defschema TimeType {:timetype String
                       :description String})

(s/defschema UserState {:user_id s/Int
                        (s/optional-key :click_time) s/Int
                        :note String})

(s/defschema Project {:user_id s/Int
                      :proj_desc String})

(s/defschema Task {:proj_id s/Int
                   :task_desc String})

(s/defschema Bubble {:task_id s/Int})

;;TODO desc_text is better than 

(defn create-timetype! [timetype]
  (db/create-timetype! {:time_type (:timetype timetype)
                        :description (:description timetype)}))

(defn update-user-state! [userstate]
  (db/update-user-state! (assoc userstate :click_time nil)))

(defapi service-routes
  {:swagger
   {:ui "/swagger-ui"
    :spec "/swagger.json"}}
  (context "/api" []

   (GET "/get_types" []
        (ok (db/get-timetypes)))

   (GET "/get_user_state/:id" []
        :path-params [id :- Long]
        (let [state (db/get-user-state {:user_id id})]
              (ok (into {} (filter second state)))))

   (POST "/add_type" []
         :return TimeType
         :body [timetype TimeType]
         :summary "Add a new time type."
         (do
          (create-timetype! timetype)
          (ok timetype)))

   (POST "/update_state" []
      :return UserState
      :body [userstate UserState]
      :summary "Update the user's current state."
      (do
        (db/update-user-state!
         (merge {:click_time nil} userstate))
        (ok userstate)))

   (GET "/get_projects/:id" []
        :path-params [id :- Long]
        (let [projects (db/get-projects {:user_id id})]
          (ok projects)))

   (POST "/add_project" []
         :return Project
         :body [project Project]
         :summary "Add a new project."
         (do
           (db/create-project! project)
           (ok project)))

   (GET "/get_tasks/:id" []
        :path-params [id :- Long]
        (let [tasks (db/get-tasks {:proj_id id})]
          (ok tasks)))

   (POST "/add_task" []
         :return Task
         :body [task Task]
         :summary "Add a new task."
         (do
           (db/create-task! task)
           (ok task)))

   (GET "/get_bubbles/:id" []
        :path-params [id :- Long]
        (let [bubbles (db/get-bubbles {:task_id id})]
          (ok bubbles)))

   (POST "/add_bubble" []
         :return Bubble
         :body [bubble Bubble]
         :summary "Add a new bubble."
         (do
           (db/create-bubble! bubble)
           (ok bubble)))

   (POST "/remove_bubble" []
         :return Bubble
         :body [bubble Bubble]
         :summary "Remove a new bubble."
         (do
           (db/remove-bubble! bubble)
           (ok bubble)))

   ))
