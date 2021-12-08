(require '[clojure.string :as str])
(use 'clojure.pprint)

(defn split-on [pred coll]
    "Like split-with but does not include the delimiter in the second
    collection"
    [(take-while pred coll)
        (->> (drop-while pred coll) rest)])

(defn parse-input [codestr]
    (->> (str/split codestr #" ")
         (split-on (partial not= "|"))
         (map #(map set %))))

(defn get-input []
    (->> (slurp *in*)
         (str/split-lines)
         (map parse-input)))

(pprint (get-input))
