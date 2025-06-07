import {Link} from "react-router-dom";
import {Users, Tent, PawPrint, Utensils, Captions, Stethoscope, BookX} from "lucide-react";

export default function AdminNav() {
    return (
        <>
            <div className="w-[2px] h-6 bg-white"></div>
            <Link to="/staff" className="flex items-center gap-1 hover:text-green-200 transition">
                <Users className="w-5 h-5"/>
                Staff
            </Link>
            <Link to="/enclosures" className="flex items-center gap-1 hover:text-green-200 transition">
                <Tent className="w-5 h-5"/>
                Enclosures
            </Link>
            <Link to="/animals" className="flex items-center gap-1 hover:text-green-200 transition">
                <PawPrint className="w-5 h-5"/>
                Animals
            </Link>
            <Link to="/feedings" className="flex items-center gap-1 hover:text-green-200 transition">
                <Utensils className="w-5 h-5"/>
                Feeding schedules
            </Link>
            <Link to="/feeding-records" className="flex items-center gap-1 hover:text-green-200 transition">
                <Captions className="w-5 h-5"/>
                Feeding records
            </Link>
            <Link to="/examinations" className="flex items-center gap-1 hover:text-green-200 transition">
                <Stethoscope className="w-5 h-5"/>
                Vet examination schedules
            </Link>
            <Link to="/medical-records" className="flex items-center gap-1 hover:text-green-200 transition">
                <BookX className="w-5 h-5"/>
                Medical records
            </Link>
        </>
    );
}
