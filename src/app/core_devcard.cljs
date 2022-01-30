(ns app.core-devcard
  (:require
   [app.cards.bmi]
   [app.cards.ethers]
   [app.cards.slider]
   [devcards.core :as devcards]))

(defn ^:export main [] (devcards.core/start-devcard-ui!))

