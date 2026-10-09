import { logout } from "../services/authService";

export default function SellerPage({ onNavigate }) {
  const handleLogout = async () => {
    await logout();
    if (onNavigate) {
      onNavigate("Login");
    } else {
      window.location.href = "/";
    }
  };

  return (
    <div className="min-h-screen bg-[#171522] flex flex-col items-center justify-center gap-6">
      <h1 className="text-4xl font-bold text-white">SELLER</h1>
      <button
        onClick={handleLogout}
        className="px-6 py-2.5 rounded-xl bg-red-500 hover:bg-red-600 text-white font-medium transition-colors cursor-pointer"
      >
        Logout
      </button>
    </div>
  );
}
