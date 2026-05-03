import { C, bg, footer, kicker, metric, node, rect, text, title } from "./common.mjs";

export async function slide06(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx);
  kicker(slide, ctx, "Datos y reporte");
  title(slide, ctx, "El reporte diario se calcula desde tablas operativas, no desde datos quemados.");
  node(slide, ctx, "orders", "fecha, mesa/cliente, estado", 76, 220, 172, 84, C.white, C.green);
  node(slide, ctx, "order_items", "cantidades, subtotales", 296, 220, 172, 84, C.white, C.green);
  node(slide, ctx, "products", "nombre del producto top", 516, 220, 172, 84, C.white, C.green);
  node(slide, ctx, "app_users", "administradores destino", 736, 220, 172, 84, C.white, C.amber);
  rect(slide, ctx, 968, 190, 190, 150, C.blue2, { line: ctx.line(C.blue, 1) });
  text(slide, ctx, "DailySalesReport", 990, 216, 150, 22, { size: 15, bold: true, color: C.blue });
  text(slide, ctx, "totalSales\norders\nuniqueCustomers\naverageOrder\ntopProduct\nadminEmails", 990, 248, 150, 84, { size: 10.2, mono: true, color: C.ink });
  metric(slide, ctx, "$199.000", "Ventas detectadas en prueba local", 112, 450, 230, C.green);
  metric(slide, ctx, "2", "Ordenes del dia en la BD", 410, 450, 210, C.blue);
  metric(slide, ctx, "Lomo de Res", "Producto mas vendido", 682, 450, 220, C.amber);
  metric(slide, ctx, "jacob@...", "Admin real; placeholders filtrados", 970, 450, 230, C.red);
  footer(slide, ctx, 6, "SalesDataService: orders, order_items, products, app_users | prueba local 2026-05-02");
  return slide;
}
