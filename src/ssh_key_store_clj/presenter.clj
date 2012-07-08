(ns ssh-key-store-clj.presenter
  (:use [seesaw.core]
        [seesaw.chooser])
  (:require [ssh-key-store-clj.view :as view]
            [ssh-key-store-clj.core :as core]))

(listen view/btn-active :action
        (fn [e]
          (let [selected-key (keyword (selection view/lb-keys))
                selected-filename (-> @core/cfg :key-files selected-key :path)]               
            (core/active-key-file selected-filename)
            (alert e "actived!!"))))

(listen view/btn-choose :action
        (fn [e] (choose-file view/f :success-fn
                             (fn [fc file]
                               (config! view/tlb-choosed :text (.getAbsolutePath file))))))

(listen view/btn-add :action
        (fn [e]
          (core/collect-new-file @core/cfg (keyword (text view/txt-key)) (text view/tlb-choosed))
          (config! view/lb-keys :model (map name
                                            (keys (:key-files @core/cfg))))
          (alert e "add!!")))