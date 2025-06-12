import './App.css'
import CryptoPrices from './components/CryptoPrices'

function App() {
  return (
    <div className='min-h-screen bg-gray-100 text-gray-800'>
      <h1 className="text-3xl font-bold text-center mt-6">Crypto Trading Simulator</h1>
      <CryptoPrices />
    </div>
  )  
}

export default App
