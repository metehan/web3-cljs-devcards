(ns app.cards.ethers
  (:require
   [cljsjs.react]
   [cljsjs.react.dom]
   ["ethers" :as eth]
   [reagent.core :as r :include-macros true]
   [devcards.core :as devcards :include-macros true :refer [defcard]]))

(def state-data (r/atom {:chain-id ""}))
(def chain-link {1 "Ethereum Mainnet"
                 4 "Rinkeby Test Network"
                 56 "Binance Smart Chain"
                 97 "BinanceSC Testnet"
                 137 "Polygon"
                 250 "Fantom Opera"
                 43114 "Avalanche"})

;(def e3provider 11 #_(.Web3Provider e3/providers (.-ethereum js/window)))
;(def e3prov e3/providers)
;(def e3provider "(e3prov/Web3Provider. (.-ethereum js/window))") 
(def log js/console.log)

(defn ethereum-provider
  "Receives window.ethereum and buidls an ethers.js Web3Provider"
  [ethereum]
  (new eth/ethers.providers.Web3Provider ethereum))

(def eth-provider (ethereum-provider (.-ethereum js/window)))
(def addr1 "0x73AEc0E221e624d6065051fD82D2f09c8599a82e") ;main wallet
(def addr2 "0x7C348aE508Fe9ba458313280a253E7D46586f2c8") ; second wallet
(def addr3 "0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266") ;hardhat wallet

(def contract-addr "0xe0b7412Eb12163a748952d04c09dAdcF584A9840")
(def erc20-abi #js["function balanceOf(address owner) view returns (uint256)"; Read-Only
                   "function decimals() view returns (uint8)"; Read-Only
                   "function symbol() view returns (string)"  ; Read-Only
                   "function transfer(address to, uint amount) returns (bool)" ;Authenticated
                   "constructor(uint totalSupply)" ;Authenticated
                   "event Transfer(address indexed from, address indexed to, uint amount)"])

(def sample-erc20-bytes "0x608060405234801561001057600080fd5b506040516103bc3803806103bc83398101604081905261002f9161007c565b60405181815233906000907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9060200160405180910390a333600090815260208190526040902055610094565b60006020828403121561008d578081fd5b5051919050565b610319806100a36000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c8063313ce5671461005157806370a082311461006557806395d89b411461009c578063a9059cbb146100c5575b600080fd5b604051601281526020015b60405180910390f35b61008e610073366004610201565b6001600160a01b031660009081526020819052604090205490565b60405190815260200161005c565b604080518082018252600781526626bcaa37b5b2b760c91b6020820152905161005c919061024b565b6100d86100d3366004610222565b6100e8565b604051901515815260200161005c565b3360009081526020819052604081205482111561014b5760405162461bcd60e51b815260206004820152601a60248201527f696e73756666696369656e7420746f6b656e2062616c616e6365000000000000604482015260640160405180910390fd5b336000908152602081905260408120805484929061016a9084906102b6565b90915550506001600160a01b0383166000908152602081905260408120805484929061019790849061029e565b90915550506040518281526001600160a01b0384169033907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9060200160405180910390a350600192915050565b80356001600160a01b03811681146101fc57600080fd5b919050565b600060208284031215610212578081fd5b61021b826101e5565b9392505050565b60008060408385031215610234578081fd5b61023d836101e5565b946020939093013593505050565b6000602080835283518082850152825b818110156102775785810183015185820160400152820161025b565b818111156102885783604083870101525b50601f01601f1916929092016040019392505050565b600082198211156102b1576102b16102cd565b500190565b6000828210156102c8576102c86102cd565b500390565b634e487b7160e01b600052601160045260246000fdfea2646970667358221220d80384ce584e101c5b92e4ee9b7871262285070dbcd2d71f99601f0f4fcecd2364736f6c63430008040033")


(defn web3 [state-data]
  (let [state @state-data]
    [:div
     [:h3 "Basic operations"]
     [:button {:on-click #(log eth-provider)} "Log: ethereum-provider"] [:br]
     [:button {:on-click #(swap! state-data assoc :signer (.getSigner eth-provider))} "Save: signer"] [:br]
     [:button {:on-click #(log (:signer @state-data))} "Log: signer from state data"] [:br]
     [:button {:on-click #(log (.getSigner eth-provider))} "Log: signer"] [:br]
     [:button {:on-click (fn [] (.then (.listAccounts eth-provider) log))} "Log: accounts"] [:br]
     [:button {:on-click (fn [] (.then (.send eth-provider "eth_requestAccounts" []) log))} "Log: get accounts permission"] [:br]
     [:button {:on-click (fn [] (.then (.getBlockNumber eth-provider) log))} "Log: Block number"] [:br]
     [:button {:on-click (fn [] (.then (.getBalance eth-provider addr3) log))} "Log: Balance hex"] [:br]
     [:button {:on-click (fn [] (.then (.getBalance eth-provider addr3) #(log (eth/ethers.utils.formatEther %))))} "Log: Balance decimal"] [:br]
     [:button {:on-click (fn [] (.then (.sendTransaction (.getSigner eth-provider) #js{:to addr2 :value (eth/ethers.utils.parseEther "0.01")}) log))} "Log: Send transaction"] [:br] [:br]
     [:h3 "Contract operations"]
     [:button {:on-click #(swap! state-data assoc :contract (new eth/Contract contract-addr erc20-abi (.getSigner eth-provider)))} "Save: contract"] [:br]
     [:button {:on-click #(log (:contract @state-data))} "Log: contract from state data"] [:br]
     [:button {:on-click (fn [] (.then (.decimals (:contract @state-data)) log))} "Log: Erc20 decimals"] [:br]
     [:button {:on-click (fn [] (.then (.symbol (:contract @state-data)) log))} "Log: Erc20 symbol"] [:br]
     [:button {:on-click (fn [] (.then ((aget (:contract @state-data) "balanceOf") addr1) log))} "Log: Erc20 Balance"] [:br]
     [:button {:on-click (fn [] (.then (.balanceOf (:contract @state-data) addr1) log))} "Log: Erc20 Balance"] [:br]
     [:button {:on-click #(log (eth/ethers.utils.parseUnits "10"))} "Log: Parse unit"] [:br]
     [:button {:on-click (fn [] (.then ((aget (:contract @state-data) "transfer") addr2 (eth/ethers.utils.parseUnits "10")) log))} "Transfer Erc20"] [:br]
     [:h3 "Contract creation"]
     [:button {:on-click #(swap! state-data assoc :factory (new eth/ethers.ContractFactory erc20-abi sample-erc20-bytes (.getSigner eth-provider)))} "Save: new contract factory"] [:br]
     [:button {:on-click #(log (:factory @state-data))} "Log: contract factory"] [:br]
     [:button {:on-click #(swap! state-data assoc :new-contract (.then (.deploy (:factory @state-data) (eth/ethers.utils.parseUnits "1000000")) identity))} "Deploy new contract"] [:br]
     [:button {:on-click #(log (:new-contract @state-data))} "Log: new contract"] [:br]
     ;[:button {:on-click (fn [] (.then (.wait (aget (:new-contract @state-data) "deployTransaction")) log))} "Wait transaction"] [:br]
     [:h3 "Sign a message"]
     [:button {:on-click #(.then (.signMessage (.getSigner eth-provider) "I agree the terms") log)} "Log: Sign a message"] [:br]
     [:h3 "Chain " (:chain-id @state-data)]
     [:button {:on-click (fn [] (.then (.getChainId (.getSigner eth-provider) "I agree the terms") #(swap! state-data assoc :chain-id %)))} "Save: ChainID"] [:br]
     [:h3 "Events "] ;https://docs.ethers.io/v5/api/providers/provider/#Provider--event-methods
     [:button {:on-click (fn [] (.on eth-provider "block" #(swap! state-data assoc :block %)))} "Listen: Blocks " (:block @state-data)] [:br]
     [:button {:on-click (fn [] (.on js/window.ethereum "chainChanged" #(swap! state-data assoc :chain-id %)))} "Listen: Metamask Chain Change "] [:br]
     [:button {:on-click (fn [] (.on js/window.ethereum "accountsChanged" #(swap! state-data assoc :account %)))} "Listen: Metamask Account " (:account @state-data)] [:br]]))

(defcard w3card
  (devcards/reagent web3)
  state-data
  {:inspect-data false
   :frame true
   :history false})