(ns app.abi.contracts)

(def contracts
  {:rinkeby {:nft {:abistr #js["constructor()"
                               "event Approval(address indexed,address indexed,uint256 indexed)"
                               "event ApprovalForAll(address indexed,address indexed,bool)"
                               "event NewEpicNFTMinted(address,uint256)"
                               "event Transfer(address indexed,address indexed,uint256 indexed)"
                               "function approve(address,uint256)"
                               "function balanceOf(address) view returns (uint256)"
                               "function getApproved(uint256) view returns (address)"
                               "function isApprovedForAll(address,address) view returns (bool)"
                               "function makeAnEpicNFT()"
                               "function name() view returns (string)"
                               "function ownerOf(uint256) view returns (address)"
                               "function pickRandomFirstWord(uint256) view returns (string)"
                               "function pickRandomSecondWord(uint256) view returns (string)"
                               "function pickRandomThirdWord(uint256) view returns (string)"
                               "function safeTransferFrom(address,address,uint256)"
                               "function safeTransferFrom(address,address,uint256,bytes)"
                               "function setApprovalForAll(address,bool)"
                               "function supportsInterface(bytes4) view returns (bool)"
                               "function symbol() view returns (string)"
                               "function tokenURI(uint256) view returns (string)"
                               "function transferFrom(address,address,uint256)"]
                   :addr "0x9CE7d7F754481D8d89Cd5b5F43fb0e1c199a19f7"
                   :name "My Epic NFT"
                   :chain :rinkeby
                   :type :nft}}
   :utils {:erc20
           {:abistr #js["function balanceOf(address owner) view returns (uint)"
                        "function transfer(address to, uint amount)"
                        "function transferFrom(address from, address to, uint amount)"
                        "function approve(address spender, uint amount)"
                        "function allowance(address owner, address spender) external view returns (uint)"]
            :type :utility
            :name "ERC20"}}})