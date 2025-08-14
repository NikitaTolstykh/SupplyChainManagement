import React, {useState} from "react";
import Button from "../../components/ui/Button";
import {useLogin} from "../../hooks/auth/useLogin";
import {useNavigate} from "react-router-dom";
import {useAuthStore} from "../../store/authStore";
import {RoleValues} from "../../lib/types/Role";

const LoginPage: React.FC = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const loginMutation = useLogin();
    const navigate = useNavigate();
    const setToken = useAuthStore((state) => state.setToken);
    const setUser = useAuthStore((state) => state.setUser);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setError("");

        loginMutation.mutate(
            {email, password},
            {
                onSuccess: (data) => {
                    const token = data.token;
                    const role = data.role;

                    if (!token || !role) {
                        setError("Invalid response from server.");
                        return;
                    }

                    setToken(token);
                    setUser({email, role});

                    if (role === RoleValues.ADMIN) {
                        navigate("/admin", {replace: true});
                    } else if (role === RoleValues.CLIENT) {
                        navigate("/client", {replace: true});
                    } else if (role === RoleValues.DISPATCHER) {
                        navigate("/dispatcher", {replace: true});
                    } else if (role === RoleValues.DRIVER) {
                        navigate("/driver", {replace: true});
                    } else {
                        navigate("/", {replace: true});
                    }
                },
                onError: () => {
                    setError("Login failed. Please check your credentials.");
                },
            }
        );
    };

    return (
        <div
            className="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 flex items-center justify-center">
            <form
                onSubmit={handleSubmit}
                className="max-w-md mx-auto bg-white rounded-2xl shadow-xl p-8 w-full"
            >
                <h2 className="text-3xl font-bold text-gray-900 text-center mb-8">
                    Login
                </h2>

                {error && (
                    <div className="mb-4 p-3 bg-red-100 border border-red-300 text-red-700 rounded-lg">
                        {error}
                    </div>
                )}

                <label htmlFor="email" className="block mb-2 font-semibold">
                    Email
                </label>
                <input
                    id="email"
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
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
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent mb-8"
                    required
                />

                <Button
                    type="submit"
                    variant="primary"
                    className="w-full py-3"
                    disabled={loginMutation.isPending}
                >
                    {loginMutation.isPending ? "Logging in..." : "Login"}
                </Button>

                <p className="mt-6 text-center">
                    Don't have an account?{" "}
                    <a href="/register" className="text-blue-600 hover:text-blue-700">
                        Register
                    </a>
                </p>
            </form>
        </div>
    );
};

export default LoginPage;
