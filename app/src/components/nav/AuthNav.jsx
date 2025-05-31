import { Link } from "react-router-dom";
import { LogIn, LogOut, UserPlus } from "lucide-react";

export default function AuthNav({ isLoggedIn, onLogout }) {
    return isLoggedIn ? (
        <button onClick={onLogout} className="flex items-center gap-1 hover:text-green-200 transition">
            <LogOut className="w-5 h-5" />
            Logout
        </button>
    ) : (
        <>
            <Link to="/login" className="flex items-center gap-1 hover:text-green-200 transition">
                <LogIn className="w-5 h-5" />
                Login
            </Link>
            <Link to="/register" className="flex items-center gap-1 hover:text-green-200 transition">
                <UserPlus className="w-5 h-5" />
                Sign Up
            </Link>
        </>
    );
}
