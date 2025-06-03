import {Route} from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute.jsx";
import CaretakerFeedingsPage from "../pages/CaretakerFeedingsPage.jsx";

const CaretakerRoutes = [
    <Route path="/my/feedings" element={<ProtectedRoute requiredRoles={["CARETAKER"]}>
        <CaretakerFeedingsPage/>
    </ProtectedRoute>}/>,

];

export default CaretakerRoutes;
