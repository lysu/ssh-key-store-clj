(ns ssh-key-store-clj.core
  (:use [seesaw.core])
  (:require [ssh-key-store-clj.fs :as fs])
  (:import java.lang.System))

(defn- display-in
  "render content in frame"
  [frame content]
  (config! frame :content content)
  content)

(def ^:const home-path
  (str (java.lang.System/getenv "HOME")))

(def ^:const key-store-dir
  (str home-path "/.ssh/bk"))

(def ^:const rsa_id-file
  (str home-path "/.ssh/rsa_id"))

(def ^:const rsa_id_pub-file
  (str home-path "/.ssh/rsa_id.pub"))

(defn- to-pub-file
  "map private key filename to pub filename"
  [filename]
  (str filename ".pub"))

(defn- active-key-file 
  "active key file"
  [filename]
  (fs/copy-key-file filename rsa_id-file)
  (fs/copy-key-file (to-pub-file filename) rsa_id_pub-file))

(defn -main 
  "main entry point for invoke seesaw"
  [& arg]
  (let [f (frame :title "SSH Private Key Manager"
                 :on-close :exit
                 :width 200
                 :height 200)]
    (invoke-later
     (native!)
     (-> f pack! show!)
     (let [cfg (fs/init-key-store key-store-dir)
           lb-keys (listbox :model
                            (map name (keys (:key-files cfg))))
           btn-active (button :text "active")]
       (display-in f (border-panel
                      :center (scrollable lb-keys)
                      :south (horizontal-panel
                              :items [btn-active])))
       (listen btn-active :action
               (fn [e]
                 (let [selected-key (keyword (selection lb-keys))
                       selected-filename (str key-store-dir "/"
                                              (-> cfg
                                                  :key-files
                                                  selected-key
                                                  :path))]
                   (active-key-file selected-filename)
                   (alert e "actived!!"))))))))






