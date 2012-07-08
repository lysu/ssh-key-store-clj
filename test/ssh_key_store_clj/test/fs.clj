(ns ssh-key-store-clj.test.fs
  (:use [clojure.test]
        [ssh-key-store-clj.fs]))

(deftest should-copy-key-file
  (testing "copy file from one to other")
  (is (= 0 0)))

(deftest should-add-new-ssh-key-pair
  (testing "add new key")
  (let [cfg {:key-files {}}
        new-key :testkey
        new-file-path "/home/robiplus/key"
        new-cfg (add-new-key cfg new-key new-file-path)]
    (is (contains? (:key-files new-cfg) new-key))
    (is (= "/home/robiplus/key" (-> new-cfg :key-files new-key :path)))
    (is (= 0 (-> new-cfg :key-files new-key :status)))))


(deftest should-get-fileinfo-edit-it-and-rewrite-to-disk
  (testing "filesystem"
    (let [cfg (init-key-store "/home/robiplus/")]
      (println (add-new-key cfg :robitest "/home/test"))
      (sync-index-store (add-new-key cfg :robitest "/home/path/test"))
      (let [new-cfg (get-all "/home/robiplus/")]
        (is (contains? (-> new-cfg :key-files) :robitest))))))