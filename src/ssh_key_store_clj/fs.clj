(ns ssh-key-store-clj.fs
  (:use [clojure.java.io :only (file copy reader writer)]
        [clojure.string :only (join split)])
  (:import java.io.File))

(defn- construct-entry 
  "construct info from one line"
  [line] 
  (let [[key value status] (split line #",")]
    {(keyword key) {:path value :status status}}))

(defn- destruct-entry 
  "deconstruct info to fs suitable format"
  [output entry]
  (conj output (join "," [(-> entry first name)
                          (-> entry second :path)
                          (-> entry second :status)])))

(defn copy-key-file 
  "copy key-file to active path"
  [key-file active-path]
  (copy (file key-file) (file active-path)))

(defn get-all
  "show all recorded key-files"
  [store-dir]
  (let [index-file (file store-dir "key-index.dat")]
    (with-open [rdr (reader index-file)]
      (let [key-infos (reduce conj {} (map construct-entry (line-seq rdr)))]
        {:root-dir store-dir :key-files key-infos}))))

(defn init-key-store 
  "init store file of ssh-key file"
  [store-dir-path]
  (.mkdir (file store-dir-path))
  (let [index-file (file store-dir-path "key-index.dat")]
    (.createNewFile index-file)
    (get-all store-dir-path)))

(defn sync-index-store 
  "sync key-index info"
  [key-files-info]
  (let [index-file (file (:root-dir key-files-info) "key-index.dat")]
    (with-open [w (writer index-file :append true)]
      (.write w
              (join "\\n"
                    (reduce destruct-entry [] (:key-files key-files-info)))))))

(defn add-new-key
  "add new key to store"
  [files-info new-key new-file-path]
  (assoc-in files-info [:key-files new-key] {:path new-file-path :status 0}))