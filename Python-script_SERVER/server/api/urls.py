from django.urls import path

from . import views
urlpatterns = [
    path('', views.index, name='index'),
    path('v1/get_prediction', views.get_data, name='get_data'),
    path('v1/get_ac_pred', views.get_ac_status, name='get_ac_status'),
]