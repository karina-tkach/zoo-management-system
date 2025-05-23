import { Home, PawPrint, Info, LogIn, LogOut, UserPlus, Users, Map, PartyPopper, Currency,
Ticket} from "lucide-react";
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
    const isTicketAgent = user?.roles.includes("TICKET_AGENT");

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
    if (loading) {
        return <></>;
    }
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
                    {isAdmin && (
                        <Link to="/staff" className="flex items-center gap-1 hover:text-green-200 transition">
                            <Users className="w-5 h-5" />
                            Staff
                        </Link>
                    )}
                    {isEventManager && (
                        <>
                            <Link to="/excursions" className="flex items-center gap-1 hover:text-green-200 transition">
                            <Map className="w-5 h-5" />
                            Excursions
                        </Link>
                        <Link to="/events" className="flex items-center gap-1 hover:text-green-200 transition">
                        <PartyPopper className="w-5 h-5" />
                        Events
                        </Link>
                        </>
                    )}
                    {isTicketAgent && (
                        <>
                            <Link to="/pricings" className="flex items-center gap-1 hover:text-green-200 transition">
                                <Currency className="w-5 h-5" />
                                Ticket Pricings
                            </Link>
                            <Link to="/tickets" className="flex items-center gap-1 hover:text-green-200 transition">
                                <Ticket className="w-5 h-5" />
                                Tickets
                            </Link>
                            {/*<Link to="/visits" className="flex items-center gap-1 hover:text-green-200 transition">
                                <PartyPopper className="w-5 h-5" />
                                Visits
                            </Link>*/}
                        </>
                    )}
                    {(!isLoggedIn || isVisitor) && (
                        <><Link
                            to="/view/events"
                            className="flex items-center gap-1 hover:text-green-200 transition"
                        >
                            <PartyPopper className="w-5 h-5" />
                            Events
                        </Link>
                        <Link
                            to="/view/excursions"
                            className="flex items-center gap-1 hover:text-green-200 transition"
                        >
                            <Map className="w-5 h-5" />
                            Excursions
                        </Link>
                            <Link
                                to="/buy-ticket"
                                className="flex items-center gap-1 hover:text-green-200 transition"
                            >
                                <Ticket className="w-5 h-5" />
                                Buy Ticket
                            </Link>
                        <Link
                        to="/animals"
                        className="flex items-center gap-1 hover:text-green-200 transition"
                    >
                        <PawPrint className="w-5 h-5" />
                        Animals
                    </Link>
                        <Link
                        to="/about"
                        className="flex items-center gap-1 hover:text-green-200 transition"
                        >
                        <Info className="w-5 h-5" />
                        About Us
                        </Link></>)}

                    {
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
                        
                    )}
                </nav>
            </div>
        </header>
    );
}
