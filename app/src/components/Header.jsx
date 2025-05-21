import { Home, PawPrint, Info, LogIn, LogOut, UserPlus, Users, Map, PartyPopper } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import {useEffect} from "react";

export default function Header() {
    const { user, loading, fetchUser } = useAuth();
    const navigate = useNavigate();
    const isLoggedIn = user && user.username !== null;
    const isAdmin = user?.roles.includes("ADMIN");
    const isEventManager = user?.roles.includes("EVENT_MANAGER");
    const isVisitor = user?.roles.includes("VISITOR");

    useEffect(() => {
        fetchUser();
    }, []);

    const handleLogout = async () => {
        try {
            await fetch("/api/auth/logout", {
                method: "POST",
                credentials: "include"
            });
            navigate("/login");
            await fetchUser();
        } catch (error) {
            navigate('/error', {
                state: {
                    message: "Something went wrong",
                    code: 500
                }
            });
        }
    };

    return (
        <header className="w-full bg-green-800 shadow-md py-4 px-6">
            <div className="max-w-7xl mx-auto flex items-center justify-between">
                <Link
                    to="/"
                    className="flex items-center gap-2 text-white font-bold text-xl hover:text-green-200 transition"
                >
                    <Home className="w-6 h-6" />
                    Our Zoo
                </Link>

                <nav className="flex items-center gap-6 text-white font-medium">
                    {!loading && isAdmin && (
                        <Link to="/staff" className="flex items-center gap-1 hover:text-green-200 transition">
                            <Users className="w-5 h-5" />
                            Staff
                        </Link>
                    )}
                    {!loading && isEventManager && (
                        <Link to="/excursions" className="flex items-center gap-1 hover:text-green-200 transition">
                            <Map className="w-5 h-5" />
                            Excursions
                        </Link>
                    )}
                    {!loading && isEventManager && (
                        <Link to="/events" className="flex items-center gap-1 hover:text-green-200 transition">
                            <PartyPopper className="w-5 h-5" />
                            Events
                        </Link>
                    )}
                    {(!isLoggedIn || isVisitor) && (<Link
                        to="/view/events"
                        className="flex items-center gap-1 hover:text-green-200 transition"
                    >
                        <PartyPopper className="w-5 h-5" />
                        Events
                    </Link>)}
                    {(!isLoggedIn || isVisitor) && (<Link
                        to="/view/excursions"
                        className="flex items-center gap-1 hover:text-green-200 transition"
                    >
                        <Map className="w-5 h-5" />
                        Excursions
                    </Link>)}
                    {(!isLoggedIn || isVisitor) && (<Link
                        to="/animals"
                        className="flex items-center gap-1 hover:text-green-200 transition"
                    >
                        <PawPrint className="w-5 h-5" />
                        Animals
                    </Link>)}
                    {(!isLoggedIn || isVisitor) && (<Link
                        to="/about"
                        className="flex items-center gap-1 hover:text-green-200 transition"
                    >
                        <Info className="w-5 h-5" />
                        About Us
                    </Link>)}

                    {!loading && (
                        isLoggedIn ? (
                            <button
                                onClick={handleLogout}
                                className="flex items-center gap-1 hover:text-green-200 transition"
                            >
                                <LogOut className="w-5 h-5"/>
                                Logout
                            </button>
                        ) : (
                            <>
                                <Link
                                    to="/login"
                                    className="flex items-center gap-1 hover:text-green-200 transition"
                                >
                                    <LogIn className="w-5 h-5"/>
                                    Login
                                </Link>
                                <Link
                                    to="/register"
                                    className="flex items-center gap-1 hover:text-green-200 transition"
                                >
                                    <UserPlus className="w-5 h-5" />
                                    Sign Up
                                </Link>
                            </>
                        )
                    )}
                </nav>
            </div>
        </header>
    );
}
