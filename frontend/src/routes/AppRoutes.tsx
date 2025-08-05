import { Routes, Route } from 'react-router-dom';
import LoginPage from "../pages/auth/LoginPage.tsx";
import RegisterPage from "../pages/auth/RegisterPage.tsx";

const AppRoutes= () => {
    return(
        <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element ={<RegisterPage />} />
        </Routes>

    );
};

export default AppRoutes