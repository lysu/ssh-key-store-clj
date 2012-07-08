(ns ssh-key-store-clj.main
  (:use [seesaw.core])
  (:require [ssh-key-store-clj.fs :as fs]
            [ssh-key-store-clj.core :as core]
            [ssh-key-store-clj.view :as view]
            [ssh-key-store-clj.presenter :as presenter]))

(defn -main 
  "main entry point for invoke seesaw"
  [& arg]
  (invoke-later
   (-> view/f pack! show!)
   (config! view/lb-keys :model (map name (keys (:key-files @core/cfg))))))