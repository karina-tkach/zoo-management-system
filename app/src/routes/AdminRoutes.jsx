import { Route } from "react-router-dom";
import StaffPage from "../pages/StaffPage.jsx";
import StaffForm from "../pages/StaffForm.jsx";
import ProtectedRoute from "./ProtectedRoute.jsx";

const AdminRoutes = [
        <Route path="/staff" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
            <StaffPage/>
        </ProtectedRoute>} />,
        <Route path="/staff/add" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
            <StaffForm/>
        </ProtectedRoute>} />,
        <Route path="/staff/edit/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
            <StaffForm/>
        </ProtectedRoute>} />,


];

export default AdminRoutes;
