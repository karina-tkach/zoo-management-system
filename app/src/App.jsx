import {Route, Routes} from "react-router-dom";
import Header from './components/Header';
import PublicRoutes from "./routes/PublicRoutes.jsx";
import EventManagerRoutes from "./routes/EventManagerRoutes.jsx";
import AdminRoutes from "./routes/AdminRoutes.jsx";
import TicketAgentRoutes from "./routes/TicketAgentRoutes.jsx";
import NotFound from "./pages/NotFound.jsx";


const App = () => {

    return (
        <>
            <Header />
            <Routes>
                {PublicRoutes}
                {AdminRoutes}
                {EventManagerRoutes}
                {TicketAgentRoutes}
                <Route path="*" element={<NotFound />} />
            </Routes>
        </>
    );
};

export default App;
