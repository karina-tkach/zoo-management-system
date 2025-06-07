import {Link} from "react-router-dom";
import {BookX, Stethoscope} from "lucide-react";

export default function VeterinarianNav() {
    return (
        <>
            <Link to="my/examinations" className="flex items-center gap-1 hover:text-green-200 transition">
                <Stethoscope className="w-5 h-5"/>
                My examination schedules
            </Link>
            <Link to="my/medical-records" className="flex items-center gap-1 hover:text-green-200 transition">
                <BookX className="w-5 h-5"/>
                My medical records
            </Link>
        </>
    );
}
