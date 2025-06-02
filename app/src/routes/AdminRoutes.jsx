import {Route} from "react-router-dom";
import StaffPage from "../pages/StaffPage.jsx";
import StaffForm from "../pages/StaffForm.jsx";
import ProtectedRoute from "./ProtectedRoute.jsx";
import EnclosuresPage from "../pages/EnclosuresPage.jsx";
import EnclosureForm from "../pages/EnclosureForm.jsx";
import EnclosureAnimalsPage from "../pages/EnclosureAnimalsPage.jsx";
import AnimalsPage from "../pages/AnimalsPage.jsx";

const AdminRoutes = [
    <Route path="/staff" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <StaffPage/>
    </ProtectedRoute>}/>,
    <Route path="/staff/add" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <StaffForm/>
    </ProtectedRoute>}/>,
    <Route path="/staff/edit/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <StaffForm/>
    </ProtectedRoute>}/>,

    <Route path="/enclosures" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <EnclosuresPage/>
    </ProtectedRoute>}/>,
    <Route path="/enclosures/add" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <EnclosureForm/>
    </ProtectedRoute>}/>,
    <Route path="/enclosures/edit/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <EnclosureForm/>
    </ProtectedRoute>}/>,
    <Route path="/enclosures/:id/animals" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <EnclosureAnimalsPage/>
    </ProtectedRoute>}/>,

    <Route path="/animals" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <AnimalsPage/>
    </ProtectedRoute>}/>,
    /*<Route path="/animals/add" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <AnimalForm/>
    </ProtectedRoute>}/>,
    <Route path="/animals/edit/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <AnimalForm/>
    </ProtectedRoute>}/>,
    <Route path="/animals/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <AnimalDetailsPage/>
    </ProtectedRoute>}/>,*/

];

export default AdminRoutes;
