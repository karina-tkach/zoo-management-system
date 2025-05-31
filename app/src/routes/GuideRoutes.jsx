import { Route } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute.jsx";
import GuideExcursionsPage from "../pages/GuideExcursionsPage.jsx";

const GuideRoutes = [
    <Route path="/guide/excursions" element={<ProtectedRoute requiredRoles={["GUIDE"]}>
        <GuideExcursionsPage/>
    </ProtectedRoute>} />,
];

export default GuideRoutes;
