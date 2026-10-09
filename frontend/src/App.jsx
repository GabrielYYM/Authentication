import { useState, useEffect } from "react";
import SignUp from "./pages/SignUp.jsx";
import Login from "./pages/Login.jsx";
import AdminPage from "./pages/AdminPage.jsx";
import CustomerPage from "./pages/CustomerPage.jsx";
import SellerPage from "./pages/SellerPage.jsx";

const ROLE_PAGES = {
  ADMIN: "AdminPage",
  CUSTOMER: "CustomerPage",
  SELLER: "SellerPage",
};

const PAGE_REQUIRED_ROLE = {
  AdminPage: "ADMIN",
  CustomerPage: "CUSTOMER",
  SellerPage: "SELLER",
};

function getHomeForRole(role) {
  const normalized = (role || "").toUpperCase();
  return ROLE_PAGES[normalized] || "CustomerPage";
}

export default function App() {
  const [activePage, setActivePage] = useState(() => {
    const thymeleafTarget = typeof window !== "undefined" ? window.__THYMELEAF_DATA__?.targetPage : null;
    const token = localStorage.getItem("authToken");
    const role = (localStorage.getItem("userRole") || "").toUpperCase();

    if (thymeleafTarget && token) {
      const requiredRole = PAGE_REQUIRED_ROLE[thymeleafTarget];
      if (!requiredRole || role === requiredRole) {
        return thymeleafTarget;
      }
    }

    if (!token) return "Login";
    return getHomeForRole(role);
  });

  const handleNavigate = (targetPage) => {
    const isAuth = !!localStorage.getItem("authToken");
    const role = (localStorage.getItem("userRole") || "").toUpperCase();

    // Páginas públicas
    if (targetPage === "Login" || targetPage === "SignUp") {
      setActivePage(targetPage);
      return;
    }

    // Se não estiver logado, redireciona para Login
    if (!isAuth) {
      setActivePage("Login");
      return;
    }

    // Se tentar acessar rota sem permissão da role, redireciona para Login
    const requiredRole = PAGE_REQUIRED_ROLE[targetPage];
    if (requiredRole && role !== requiredRole) {
      setActivePage("Login");
      return;
    }

    setActivePage(targetPage);
  };

  useEffect(() => {
    const isAuth = !!localStorage.getItem("authToken");
    const role = (localStorage.getItem("userRole") || "").toUpperCase();

    if (!isAuth && activePage !== "Login" && activePage !== "SignUp") {
      setActivePage("Login");
    } else if (isAuth && (activePage === "Login" || activePage === "SignUp")) {
      setActivePage(getHomeForRole(role));
    } else if (isAuth && PAGE_REQUIRED_ROLE[activePage]) {
      const requiredRole = PAGE_REQUIRED_ROLE[activePage];
      if (role !== requiredRole) {
        setActivePage("Login");
      }
    }
  }, [activePage]);

  const pages = {
    SignUp: <SignUp onNavigate={handleNavigate} />,
    Login: <Login onNavigate={handleNavigate} />,
    AdminPage: <AdminPage onNavigate={handleNavigate} />,
    CustomerPage: <CustomerPage onNavigate={handleNavigate} />,
    SellerPage: <SellerPage onNavigate={handleNavigate} />,
  };

  return pages[activePage] || <Login onNavigate={handleNavigate} />;
}
