import {Route} from "react-router-dom";
import StaffPage from "../pages/StaffPage.jsx";
import StaffForm from "../pages/StaffForm.jsx";
import ProtectedRoute from "./ProtectedRoute.jsx";
import EnclosuresPage from "../pages/EnclosuresPage.jsx";
import EnclosureForm from "../pages/EnclosureForm.jsx";
import EnclosureAnimalsPage from "../pages/EnclosureAnimalsPage.jsx";
import AnimalsPage from "../pages/AnimalsPage.jsx";
import AnimalForm from "../pages/AnimalForm.jsx";
import AnimalDetailsPage from "../pages/AnimalDetailsPage.jsx";
import FeedingsPage from "../pages/FeedingsPage.jsx";
import FeedingForm from "../pages/FeedingsForm.jsx";
import FeedingRecordsPage from "../pages/FeedingRecordsPage.jsx";
import AnimalFeedingsPage from "../pages/AnimalFeedingsPage.jsx";
import MedicalRecordsPage from "../pages/MedicalRecordsPage.jsx";
import ExaminationsPage from "../pages/ExaminationsPage.jsx";
import AnimalExaminationsPage from "../pages/AnimalExaminationsPage.jsx";
import ExaminationsForm from "../pages/ExaminationsForm.jsx";

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
    <Route path="/animals/add" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <AnimalForm/>
    </ProtectedRoute>}/>,
    <Route path="/animals/edit/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <AnimalForm/>
    </ProtectedRoute>}/>,
    <Route path="/animals/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <AnimalDetailsPage/>
    </ProtectedRoute>}/>,

    <Route path="/feedings" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <FeedingsPage/>
    </ProtectedRoute>}/>,
    <Route path="/feedings/add" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <FeedingForm/>
    </ProtectedRoute>}/>,
    <Route path="/feedings/edit/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <FeedingForm/>
    </ProtectedRoute>}/>,
    <Route path="/animals/feedings/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <AnimalFeedingsPage/>
    </ProtectedRoute>}/>,
    <Route path="/feeding-records" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <FeedingRecordsPage/>
    </ProtectedRoute>}/>,

    <Route path="/examinations" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <ExaminationsPage/>
    </ProtectedRoute>}/>,
    <Route path="/animals/examinations/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <AnimalExaminationsPage/>
    </ProtectedRoute>}/>,
    <Route path="/examinations/add" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <ExaminationsForm/>
    </ProtectedRoute>}/>,
    <Route path="/examinations/edit/:id" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <ExaminationsForm/>
    </ProtectedRoute>}/>,

    <Route path="/medical-records" element={<ProtectedRoute requiredRoles={["ADMIN"]}>
        <MedicalRecordsPage/>
    </ProtectedRoute>}/>,

];

export default AdminRoutes;
