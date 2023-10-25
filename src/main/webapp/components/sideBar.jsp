<section id="sideBar" class="active">
    <div class="logo">
        <img src="https://cdn-icons-png.flaticon.com/512/780/780575.png" />
        <h1 class="pt-3 pl-2">Events <sup>.ma</sup></h1>
    </div>
    <div class="items">
        <a href="${pageContext.request.contextPath}/Dashboard">
            <li  class="active-link">
                <i class="fa-duotone fa-chart-pie-simple" style="--fa-secondary-opacity: 1"></i>Dashboard
            </li>
        </a>
        <a href="${pageContext.request.contextPath}/profile">
        <c:if test="${url.equals('/profile') ? 'active-link':''}">
            <li class="">
                <i class="fa-duotone fa-user" style="--fa-secondary-opacity: 1"></i>profile
            </li>
        </c:if>
        </a>
        <a href="${pageContext.request.contextPath}/event">
            <li>
                <i class="fa-duotone fa-briefcase" style="--fa-secondary-opacity: 1"></i>Event
            </li>
        </a>
        <a href="${pageContext.request.contextPath}/myBooking">
        <c:if test="${url.equals('/myBooking') ? 'active-link':''}">
            <li class="">
                <i class="fa-duotone fa-bullhorn" style="--fa-secondary-opacity: 1"></i>My booking
            </li>
        </c:if>
        </a>
    </div>
</section>
<section id="interface">
