import { Link } from "react-router-dom";
import { PartyPopper, Map, Ticket, PawPrint, Info } from "lucide-react";

export default function VisitorNav() {
    return (
        <>
            <Link to="/view/events" className="flex items-center gap-1 hover:text-green-200 transition">
                <PartyPopper className="w-5 h-5" />
                Events
            </Link>
            <Link to="/view/excursions" className="flex items-center gap-1 hover:text-green-200 transition">
                <Map className="w-5 h-5" />
                Excursions
            </Link>
            <Link to="/buy-ticket" className="flex items-center gap-1 hover:text-green-200 transition">
                <Ticket className="w-5 h-5" />
                Buy Ticket
            </Link>
            <Link to="/view/animals" className="flex items-center gap-1 hover:text-green-200 transition">
                <PawPrint className="w-5 h-5" />
                Animals
            </Link>
            <Link to="/about" className="flex items-center gap-1 hover:text-green-200 transition">
                <Info className="w-5 h-5" />
                About Us
            </Link>
        </>
    );
}
