import Login from './components/login/Login'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Register from './components/register/Register'
import Market from './components/market/Market'
import Logout from './components/logout/Logout'
import ProtectedRoute from './components/protectedRoute/ProtectedRoute'
import Wallet from './components/wallet/Wallet'
import BuyCrypto from './components/crypto/buy/BuyCrypto'
import SelectCrypto from './components/crypto/buy/SelectCrypto'
import SellCryptoList from './components/crypto/sell/SellCryptoList'
import SellCrypto from './components/crypto/sell/SellCrypto'
import Reset from './components/wallet/Reset'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/market" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/logout" element={<Logout />} />
        <Route path="/register" element={<Register />} />

        <Route path="/market" element={
          <ProtectedRoute>
            <Market />
          </ProtectedRoute>
        } />

        <Route path="/wallet" element={
          <ProtectedRoute>
            <Wallet />
          </ProtectedRoute>
        } />
        
        <Route path="/market/select" element={
          <ProtectedRoute>
            <SelectCrypto />
          </ProtectedRoute>
        }/>
        
        <Route path="/market/:symbol/buy" element={
          <ProtectedRoute>
            <BuyCrypto />
          </ProtectedRoute>
        } />
        
        <Route path="/market/sell" element={
          <ProtectedRoute>
            <SellCryptoList />
          </ProtectedRoute>
        } />
        
        <Route path="/market/:symbol/sell" element={
          <ProtectedRoute>
            <SellCrypto />
          </ProtectedRoute>
        } />
        
        <Route path="/wallet/reset" element={
          <ProtectedRoute>
            <Reset />
          </ProtectedRoute>
        } />

        <Route path="*" element={<Navigate to="/market" replace />} />        
      </Routes>
    </BrowserRouter>
  )  
}

export default App
