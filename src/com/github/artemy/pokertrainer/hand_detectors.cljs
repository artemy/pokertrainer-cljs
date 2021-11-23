(ns com.github.artemy.pokertrainer.hand-detectors)

(declare straight-flush?)
(declare d-flush?)
(declare straight?)
(declare three-of-a-kind?)
(declare pair?)

;-- Utils
(defn group-by-rank [cards] (group-by :rank cards))

(defn group-by-suit [cards] (group-by :suit cards))

(defn sort-by-rank [cards] (sort-by :rank cards))

(defn of-required-size? [size] #(-> % count (= size)))

(defn find-groups-by-rank-with-size
  [size cards]
  (->> cards group-by-rank vals (filter (of-required-size? size))))

(defn has-groups-by-rank-with-size?
  [size cards]
  (->> cards group-by-rank vals (filter (of-required-size? size)) (some not-empty)))

(defn count-pairs [cards]
  (->> cards group-by-rank vals (filter (of-required-size? 2)) count))

;-- analyzers
(defn royal-flush?
  [cards]
  (let [starts-with-10? #(-> % sort-by-rank first :rank (= 10))]
    ((every-pred straight-flush? starts-with-10?) cards)))

(defn straight-flush?
  [cards]
  ((every-pred straight? d-flush?) cards))

(defn four-of-a-kind?
  [cards]
  (has-groups-by-rank-with-size? 4 cards))

(defn full-house?
  [cards]
  ((every-pred three-of-a-kind? pair?) cards))

(defn d-flush?
  [cards]
  (let [of-size-five? #(-> % count (>= 5))]
    (->> cards group-by-suit vals (some of-size-five?))))

(defn straight?
  [cards]
  (let [sorted-ranks (->> cards sort-by-rank (map :rank) reverse distinct)
        cycled-sorted-ranks (take (-> sorted-ranks count (+ 1)) (cycle sorted-ranks))
        all-permutations-of-sorted-ranks (partition 5 1 cycled-sorted-ranks)
        calculate-differences #(->> % (partition 2 1) (map (fn [[i j]] (- i j))))
        in-sequence? #(or (= 1 %) (= -12 %))
        all-in-sequence? #(every? in-sequence? %)]
    (->> all-permutations-of-sorted-ranks (map calculate-differences) (some all-in-sequence?))))

(defn three-of-a-kind?
  [cards]
  (has-groups-by-rank-with-size? 3 cards))

(defn two-pairs?
  [cards]
  (-> cards count-pairs (>= 2)))

(defn pair?
  [cards]
  (-> cards count-pairs (= 1)))

(defn high-card?
  [_] true)

(def hand-analyzers
  (array-map :royalFlush royal-flush?
             :straightFlush straight-flush?
             :fourOfAKind four-of-a-kind?
             :fullHouse full-house?
             :flush d-flush?
             :straight straight?
             :threeOfAKind three-of-a-kind?
             :twoPairs two-pairs?
             :pair pair?
             :highCard high-card?))

(defn compute-correct-answer
  [cards]
  (->> hand-analyzers (filter #((second %) cards)) keys first))
