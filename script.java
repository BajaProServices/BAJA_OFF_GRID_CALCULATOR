const appliances = [
    { name: "Mini-Split 1.0 Ton", watts: 1100, icon: "fa-snowflake" },
    { name: "Mini-Split 1.5 Tons", watts: 1650, icon: "fa-snowflake" },
    { name: "Mini-Split 2.0 Tons", watts: 2200, icon: "fa-snowflake" },
    { name: "Refrigerador Side-by-Side", watts: 350, icon: "fa-box" },
    { name: "Horno Eléctrico Empotrable", watts: 2800, icon: "fa-fire" },
    { name: "Parrilla de Inducción", watts: 4000, icon: "fa-bolt" },
    { name: "Lavavajillas", watts: 1500, icon: "fa-water" },
    { name: "Cava de Vinos", watts: 150, icon: "fa-wine-glass" },
    { name: "Microondas Grande", watts: 1400, icon: "fa-wave-square" },
    { name: "Cafetera Profesional", watts: 1300, icon: "fa-coffee" },
    { name: "Air Fryer", watts: 1600, icon: "fa-wind" },
    { name: "Licuadora Potencia", watts: 1200, icon: "fa-blender" },
    { name: "Lavadora Carga Frontal", watts: 800, icon: "fa-tshirt" },
    { name: "Secadora Eléctrica", watts: 5000, icon: "fa-wind" },
    { name: "Hidroneumático (Bomba)", watts: 900, icon: "fa-faucet" },
    { name: "Calentador Eléctrico", watts: 4000, icon: "fa-hot-tub" },
    { name: "Pantalla OLED 75+", watts: 300, icon: "fa-tv" },
    { name: "Home Theater", watts: 400, icon: "fa-volume-up" },
    { name: "Computadora Gamer", watts: 700, icon: "fa-desktop" },
    { name: "Aspiradora Robot", watts: 50, icon: "fa-robot" },
    { name: "Secador de Cabello", watts: 2000, icon: "fa-wind" },
    { name: "Bomba de Alberca", watts: 1500, icon: "fa-water" },
    { name: "Cargador EV (Auto)", watts: 7200, icon: "fa-car" },
    { name: "Cámaras de Seguridad", watts: 80, icon: "fa-video" },
    { name: "Portón Eléctrico", watts: 450, icon: "fa-door-open" }
];

const PANEL_POWER = 550; // Watts por panel
const HOURS_SUN = 5.5;   // Promedio BCS
let totalWatts = 0;

const grid = document.getElementById('appliance-grid');

appliances.forEach((item, index) => {
    const div = document.createElement('div');
    div.className = "flex items-center justify-between p-4 bg-slate-50 rounded-2xl border-2 border-transparent hover:border-blue-100 hover:bg-white transition-all cursor-pointer group";
    div.innerHTML = `
        <div class="flex items-center gap-4">
            <div class="w-10 h-10 bg-white rounded-xl flex items-center justify-center shadow-sm group-hover:text-blue-600">
                <i class="fas ${item.icon}"></i>
            </div>
            <div>
                <div class="text-sm font-bold text-slate-700">${item.name}</div>
                <div class="text-xs text-slate-400">${item.watts}W estimación</div>
            </div>
        </div>
        <input type="checkbox" class="w-6 h-6 rounded-full text-blue-600 border-slate-300" onchange="calculate()">
    `;
    div.onclick = (e) => {
        if(e.target.type !== 'checkbox') {
            const ck = div.querySelector('input');
            ck.checked = !ck.checked;
            calculate();
        }
    };
    grid.appendChild(div);
});

function calculate() {
    totalWatts = 0;
    const checks = document.querySelectorAll('input[type="checkbox"]:checked');
    
    checks.forEach(ck => {
        const index = Array.from(document.querySelectorAll('input[type="checkbox"]')).indexOf(ck);
        totalWatts += appliances[index].watts;
    });

    // Lógica simple: (Watts totales / 1000) * factor de uso diario / (Panel * horas sol)
    // Para simplificar al usuario: Cada ~1500W de carga nominal seleccionada suele requerir 1-2 paneles en BCS
    const panels = Math.ceil(totalWatts / 1200); 
    document.getElementById('panel-result').innerText = totalWatts > 0 ? panels : 0;
}

function sendToWhatsApp() {
    const name = document.getElementById('client-name').value;
    const city = document.getElementById('client-city').value;
    const panels = document.getElementById('panel-result').innerText;

    if(!name) { alert("Por favor, ingresa tu nombre"); return; }
    if(panels == 0) { alert("Selecciona al menos un equipo"); return; }

    const phone = "5216122198325";
    const text = `*SOLICITUD DE COTIZACIÓN - BAJA PRO SERVICES*%0A%0A` +
                 `👤 *Cliente:* ${name}%0A` +
                 `📍 *Ubicación:* ${city}%0A` +
                 `⚡ *Carga Estimada:* ${totalWatts}W%0A` +
                 `☀️ *Paneles Sugeridos:* ${panels} Módulos de 550W%0A%0A` +
                 `*Promoción:* Aplicar 10% OFF (Vence 30 Jun)%0A` +
                 `_Precios sujetos a +16% IVA_`;

    window.open(`https://wa.me/${phone}?text=${text}`, '_blank');
}
