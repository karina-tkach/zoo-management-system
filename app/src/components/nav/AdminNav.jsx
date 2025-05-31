import { Link } from "react-router-dom";
import { Users } from "lucide-react";

export default function AdminNav() {
    return (
        <Link to="/staff" className="flex items-center gap-1 hover:text-green-200 transition">
            <Users className="w-5 h-5" />
            Staff
        </Link>
    );
}
