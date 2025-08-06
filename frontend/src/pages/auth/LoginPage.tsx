import React, {useState} from 'react';
import Button from '../../components/ui/Button';
import {useLogin} from "../../hooks/auth/useLogin.ts";
import {useNavigate} from 'react-router-dom'
import {useAuthStore} from "../../store/authStore.ts";
import {Role} from "../../lib/types/Role.ts";

const LoginPage: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const loginMutation = useLogin();
    const navigate = useNavigate();
    const setToken = useAuthStore(state => state.setToken);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        loginMutation.mutate(
            { email, password },
            {
                onSuccess: (data) => {
                    const token = data.token;
                    setToken(token);

                    const payload = JSON.parse(atob(token.split('.')[1]));
                    const role: Role = payload.role;


                    if (role === Role.ADMIN) {
                        navigate('/admin');
                    } else {
                        navigate('/');
                    }
                },
                onError: (error) => {
                    alert(error.message);
                },
            }
        );
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 flex items-center justify-center">
            <form
                onSubmit={handleSubmit}
                className="max-w-md mx-auto bg-white rounded-2xl shadow-xl p-8 w-full"
            >
                <h2 className="text-3xl font-bold text-gray-900 text-center mb-8">Login</h2>

                <label htmlFor="email" className="block mb-2 font-semibold">
                    Email
                </label>
                <input
                    id="email"
                    type="email"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent mb-6"
                    required
                />

                <label htmlFor="password" className="block mb-2 font-semibold">
                    Password
                </label>
                <input
                    id="password"
                    type="password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent mb-8"
                    required
                />

                <Button type="submit" variant="primary" className="w-full py-3" disabled={loginMutation.isPending}>
                    {loginMutation.isPending ? 'Logging in...' : 'Login'}
                </Button>

                <p className="mt-6 text-center">
                    Don't have an account?{' '}
                    <a href="/register" className="text-blue-600 hover:text-blue-700 font-medium">
                        Register
                    </a>
                </p>
            </form>
        </div>
    );
};

export default LoginPage;