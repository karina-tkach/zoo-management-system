import { Link } from "react-router-dom";
import { Currency, Ticket, Store, ScrollText } from "lucide-react";

export default function TicketAgentNav() {
    return (
        <>
            <Link to="/pricings" className="flex items-center gap-1 hover:text-green-200 transition">
                <Currency className="w-5 h-5" />
                Ticket Pricings
            </Link>
            <Link to="/tickets" className="flex items-center gap-1 hover:text-green-200 transition">
                <Ticket className="w-5 h-5" />
                Tickets
            </Link>
            <Link to="/gates" className="flex items-center gap-1 hover:text-green-200 transition">
                <Store className="w-5 h-5" />
                Gates
            </Link>
            <Link to="/visits" className="flex items-center gap-1 hover:text-green-200 transition">
                <ScrollText className="w-5 h-5" />
                Visit logs
            </Link>
        </>
    );
}
