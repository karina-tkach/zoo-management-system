import { Home} from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import {useEffect} from "react";
import AdminNav from "./nav/AdminNav.jsx";
import EventManagerNav from "./nav/EventManagerNav.jsx";
import TicketAgentNav from "./nav/TicketAgentNav.jsx";
import VisitorNav from "./nav/VisitorNav.jsx";
import AuthNav from "./nav/AuthNav.jsx";
import GuideNav from "./nav/GuideNav.jsx";
import CaretakerNav from "./nav/CaretakerNav.jsx";

export default function Header() {
    const { user, loading, fetchUser } = useAuth();
    const navigate = useNavigate();
    const isLoggedIn = user && user.username !== null;
    const isAdmin = user?.roles.includes("ADMIN");
    const isEventManager = user?.roles.includes("EVENT_MANAGER");
    const isVisitor = user?.roles.includes("VISITOR");
    const isTicketAgent = user?.roles.includes("TICKET_AGENT");
    const isGuide = user?.roles.includes("GUIDE");
    const isCaretaker = user?.roles.includes("CARETAKER");

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
                    {isAdmin && <AdminNav />}
                    {isEventManager && <EventManagerNav />}
                    {isTicketAgent && <TicketAgentNav />}
                    {isGuide && <GuideNav />}
                    {isCaretaker && <CaretakerNav />}
                    {(!isLoggedIn || isVisitor) && <VisitorNav />}
                    <AuthNav isLoggedIn={isLoggedIn} onLogout={handleLogout} />
                </nav>
            </div>
        </header>
    );
}
