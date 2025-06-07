import { Route } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute.jsx";
import VetMedicalRecordsPage from "../pages/VetMedicalRecordsPage.jsx";
import VetExaminationsPage from "../pages/VetExaminationsPage.jsx";

const VetRoutes = [
    <Route path="/my/medical-records" element={<ProtectedRoute requiredRoles={["VETERINARIAN"]}>
        <VetMedicalRecordsPage/>
    </ProtectedRoute>} />,
    <Route path="/my/examinations" element={<ProtectedRoute requiredRoles={["VETERINARIAN"]}>
        <VetExaminationsPage/>
    </ProtectedRoute>} />,
];

export default VetRoutes;
